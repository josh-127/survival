package net.survival.block.io

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue

class ColumnDbPipe {
    private val requests = LinkedBlockingQueue<ColumnRequest>()
    private val responses = ConcurrentLinkedQueue<ColumnResponse>()

    fun waitForRequest(): ColumnRequest = requests.take()

    fun respond(response: ColumnResponse) = responses.add(response)

    fun pollResponse(): ColumnResponse? = responses.poll()

    fun request(request: ColumnRequest) = requests.add(request)
}