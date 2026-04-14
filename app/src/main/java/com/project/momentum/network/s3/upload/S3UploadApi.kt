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
        byteSize: Long,
        onProgress: (Int) -> Unit = {}
    ) {
        val input = requireNotNull(contentResolver.openInputStream(uri)) {
            "Cannot open InputStream for uri=$uri"
        }

        input.use { stream ->
            onProgress(0)

            s3Client.put(uploadURL) {
                header(HttpHeaders.ContentType, mimeType)
                header(HttpHeaders.ContentLength, byteSize.toString())
                setBody(stream.asOutgoingContent(byteSize, onProgress))
            }

            onProgress(100)
        }
    }
}

private fun InputStream.asOutgoingContent(
    contentLength: Long,
    onProgress: (Int) -> Unit
) =
    object : OutgoingContent.WriteChannelContent() {
        override val contentLength: Long = contentLength

        override suspend fun writeTo(channel: ByteWriteChannel) {
            withContext(Dispatchers.IO) {
                val output = channel.toOutputStream()
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var uploadedBytes = 0L
                var lastProgress = -1

                while (true) {
                    val read = this@asOutgoingContent.read(buffer)
                    if (read == -1) break

                    output.write(buffer, 0, read)
                    uploadedBytes += read

                    if (contentLength > 0) {
                        val progress = ((uploadedBytes * 100) / contentLength)
                            .toInt()
                            .coerceIn(0, 100)

                        if (progress != lastProgress) {
                            lastProgress = progress
                            onProgress(progress)
                        }
                    }
                }

                output.flush()
            }
        }
    }
