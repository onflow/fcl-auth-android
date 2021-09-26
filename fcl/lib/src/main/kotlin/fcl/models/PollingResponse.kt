package fcl.models

data class PollingResponse (
    val status: String,
    val data: PollingData?,
    val updates: Service?,
    val local: Service?
)

data class PollingData (
    val addr: String,
    val services: Array<Service>?
)

data class Service(
    val type: String?,
    val method: String?,
    val endpoint: String
)