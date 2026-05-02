package com.project.momentum.network.s3.upload

import android.content.ContentResolver
import android.net.Uri
import com.project.momentum.network.di.S3
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
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
import io.ktor.utils.io.writeFully
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
        byteSize: Long,
        onProgress: (Long, Long?) -> Unit = { _, _ -> }
    ) {

        require(byteSize > 0L) {
            "Cannot upload uri=$uri because byteSize=$byteSize"
        }

        val input = requireNotNull(contentResolver.openInputStream(uri)) {
            "Cannot open InputStream for uri=$uri"
        }

        input.use { stream ->

            val response = s3Client.put(uploadURL) {
                header(HttpHeaders.ContentType, mimeType)
                header(HttpHeaders.ContentLength, byteSize.toString())
                setBody(stream.asOutgoingContent(byteSize))

                onUpload { bytesSentTotal, contentLength ->
                    onProgress(bytesSentTotal, contentLength ?: byteSize)
                }
            }

        }
    }
}

private fun InputStream.asOutgoingContent(
    size: Long
) =
    object : OutgoingContent.WriteChannelContent() {
        override val contentLength: Long = size

        override suspend fun writeTo(channel: ByteWriteChannel) {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

            while (true) {
                val read = withContext(Dispatchers.IO) {
                    this@asOutgoingContent.read(buffer)
                }

                if (read == -1) break

                channel.writeFully(buffer, 0, read)
            }

            channel.flush()
        }
    }

