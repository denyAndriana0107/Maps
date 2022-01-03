package danu.ga.maps.api.model

import com.google.gson.annotations.SerializedName

data class KordinatResponse(

	@field:SerializedName("body")
	val body: List<BodyItem?>? = null
)

data class BodyItem(

	@field:SerializedName("Img")
	val img: String? = null,

	@field:SerializedName("Nama")
	val nama: String? = null,

	@field:SerializedName("Lng")
	val lng: Double? = null,

	@field:SerializedName("Waktu Buka")
	val waktuBuka: Int? = null,

	@field:SerializedName("Waktu Tutup")
	val waktuTutup: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("Lat")
	val lat: Double? = null
)
