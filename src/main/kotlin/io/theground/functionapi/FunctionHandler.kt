package io.theground.functionapi

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.io.File
import java.io.InputStream
import java.io.SequenceInputStream

@Component
class FunctionHandler {

    @Autowired
    val amazonS3: AmazonS3? = null

    @Value("\${s3.bucket}")
    val bucket : String? = null

    fun submit (request : ServerRequest) : Mono<ServerResponse> {

        // Not used in this example, but useful for logging, etc
//        val id = request.pathVariable("id")

        return request.body(BodyExtractors.toMultipartData()).flatMap { parts ->

//            try {

                val map: Map<String, Part> = parts.toSingleValueMap()


                val filePart: FilePart = map["file"]!! as FilePart
                // Note cast to "FilePart" above

                // Save file to disk - in this example, in the "tmp" folder of a *nix system
                val fileName = filePart.filename()
                filePart.transferTo( File("/tmp/$fileName"))
//                val tb = TransferManagerBuilder.standard()
//                tb.s3Client = amazonS3
//                val transferManager = tb.build()


//                transferManager.upload(bucket, "functions/$fileName", File("/tmp/$fileName"))

//                ServerResponse.ok().body(fromObject("OK"))


//                val buffer = filePart.content()

//            filePart.content().collect(({ InputStreamCollector() }), { t , dataBuffer -> t.collectInputStream(dataBuffer.asInputStream()) })
//            .flatMap {
                amazonS3?.putObject(PutObjectRequest(bucket, "functions/$fileName", File("/tmp/$fileName").inputStream(), ObjectMetadata())
                        .withCannedAcl(CannedAccessControlList.PublicRead))
                ServerResponse.ok().body(fromObject("OK"))
//            }


//                filePart.content().reduce(object : InputStream() {
//                    override fun read() = -1
//                }) { s: InputStream, d -> SequenceInputStream(s, d.asInputStream()) }
//                        .flatMap {
//                            amazonS3?.putObject(PutObjectRequest(bucket, "functions/$fileName", it, ObjectMetadata())
//                                    .withCannedAcl(CannedAccessControlList.PublicRead))
//                            ServerResponse.ok().body(fromObject("OK"))
//                        }

//            } catch (ex: Exception) {
//                ex.printStackTrace()
//                ServerResponse.noContent().build()
//            }

        }

    }

    fun list (request: ServerRequest) =
        ServerResponse.ok().body(fromObject("listing functions!"))
}

class InputStreamCollector {
    var inputStream: InputStream? = null
        private set

    fun collectInputStream(input: InputStream) {
        if (this.inputStream == null) this.inputStream = input
        this.inputStream = SequenceInputStream(this.inputStream, input)
    }
}