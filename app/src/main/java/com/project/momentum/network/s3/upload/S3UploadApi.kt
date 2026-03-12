package com.project.momentum.network.s3.upload

import android.content.ContentResolver
import android.net.Uri
import com.project.momentum.network.di.S3
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.jvm.javaio.toOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import javax.inject.Singleton

@Singleton
class S3UploadApi @Inject constructor(
    @S3 private val s3Client: HttpClient,
    private val contentResolver: ContentResolver
) {
    suspend fun sendFileToS3(
        uploadURL: String,
        uri: Uri,
        mimeType: String,
        byteSize: Long
    ) {
        val input = requireNotNull(contentResolver.openInputStream(uri)) {
            "Cannot open InputStream for uri=$uri"
        }

        input.use { stream ->
            s3Client.put(uploadURL) {
                header(HttpHeaders.ContentType, mimeType)
                header(HttpHeaders.ContentLength, byteSize.toString())
                setBody(stream.asOutgoingContent(byteSize))
            }
        }
    }
}

private fun InputStream.asOutgoingContent(contentLength: Long) =
    object : OutgoingContent.WriteChannelContent() {
        override val contentLength: Long = contentLength
        override suspend fun writeTo(channel: ByteWriteChannel) {
            withContext(Dispatchers.IO) {
                this@asOutgoingContent.copyTo(channel.toOutputStream())
            }
        }
    }