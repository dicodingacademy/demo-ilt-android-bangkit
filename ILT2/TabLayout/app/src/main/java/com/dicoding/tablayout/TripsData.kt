package com.dicoding.tablayout

object TripsData {

    private val tripsName = arrayOf(
        "Kelimutu National Park",
        "Toba Lake",
        "Bromo Mountain",
        "Bunaken",
        "Komodo Island",
        "Dieng Plateau",
        "Raja Ampat Islands",
        "Derawan Islands",
        "Kawah Putih",
        "Tanjung Tinggi Beach"
    )

    private val tripsDescription = arrayOf(
        "Kelimutu National Park is located on Flores, Indonesia. The national park consists of hills and mountains with Mount Kelibara (1,731 m) as the highest peak. Mount Kelimutu, which has the three-colored lake, is also located in this national park.",
        "Lake Toba is the location of a super-massive volcanic eruption with a VEI of 8 that occurred around 69,000 to 77,000 years ago and triggered global climate change. Recent dating methods have determined that 74,000 years ago is more accurate. This eruption was the largest explosive eruption on Earth in the last 25 million years.",
        "Mount Bromo (from Sanskrit: Brahma, one of the main gods in Hinduism) or spelled \"Brama\" in the Tengger language, is an active volcano in East Java, Indonesia. This mountain has a height of 2,329 meters above sea level and is located in four districts, namely Probolinggo Regency, Pasuruan Regency, Lumajang Regency, and Malang Regency.",
        "Bunaken is an island with an area of 8.08 km² in Manado Bay, located north of the island of Sulawesi, Indonesia. The island is part of the city of Manado, the capital of the province of North Sulawesi, Indonesia. Bunaken Island can be reached by speedboat or chartered boat with a journey of about 30 minutes from the port of Manado City.",
        "Komodo Island is an island located in the Nusa Tenggara Islands. Komodo Island is known as the natural habitat of the Komodo dragon. The island is also a part of the Komodo National Park which is managed by the Central Government. Komodo Island is located east of Sumbawa Island, separated by the Sape Strait.",
        "The Dieng Plateau (DTD) is a plateau with volcanic activity under its surface, like Yellowstone or the Tengger Plateau. It is actually a caldera with mountains around it as its edge. There are many craters that serve as outlets for gas, water vapor, and other volcanic materials.",
        "The Raja Ampat Islands are a group of four archipelagic islands located in the western part of the Bird's Head Peninsula (Vogelkoop) of Papua Island. Administratively, this archipelago is under the Raja Ampat Regency, West Papua Province. These islands are now a destination for divers who are interested in the beauty of its underwater scenery.",
        "Derawan Islands is an archipelago located in Berau Regency, East Kalimantan, Indonesia. The archipelago consists of several charming marine tourism objects, one of which is the Underwater Park which is popular with foreign tourists, especially world-class divers.",
        "Kawah Putih is a tourist destination in West Java, Indonesia. It is located in the village of Alam Endah, Rancabali District, Bandung Regency, West Java, at the foot of Mount Patuha. Kawah Putih is a lake formed from the eruption of Mount Patuha.",
        "The uniqueness of this beach is the granite stones that vary in size, from a few cubic meters to hundreds of cubic meters. When the sun starts to set, Tanjung Tinggi Beach will radiate its extraordinary beauty. With its unique beauty, Tanjung Tinggi Beach is known by many tourists from both local and abroad."
    )

    private val tripsPrice = arrayOf(
        300,
        200,
        225,
        250,
        400,
        150,
        450,
        300,
        200,
        150
    )

    private val tripsImage = arrayOf(
        R.drawable.tnkelimutu,
        R.drawable.laketoba,
        R.drawable.bromomountain,
        R.drawable.bunaken,
        R.drawable.komodoisland,
        R.drawable.dieng,
        R.drawable.rajaampat,
        R.drawable.derawan,
        R.drawable.kawahputih,
        R.drawable.tanjungtinggibeach
    )

    private val tripsPlace = arrayOf(
        "Detusoko, Ende Regency, NTT",
        "Pematang Siantar City, North Sumatra",
        "Podokoyo, Tosari, Pasuruan",
        "Bunaken, Minahasa Regency, North Sulawesi",
        "West Manggarai Regency, NTT",
        "Batur, Banjarnegara, Central Java",
        "Raja Ampat Regency, West Papua",
        "Berau Regency, East Kalimantan",
        "Sugihmukti, Pasirjambu District, Bandung",
        "Sijuk District, Belitung Regency, Bangka Belitung"
    )

    val tripsTourGuides = arrayListOf<ArrayList<Int>>(
        arrayListOf(6, 11, 14, 17, 15),
        arrayListOf(3, 12, 18),
        arrayListOf(0, 4, 8, 10, 16),
        arrayListOf(5, 13, 19),
        arrayListOf(1, 7, 9),
        arrayListOf(11, 12, 2, 18),
        arrayListOf(15, 5, 9),
        arrayListOf(0, 3, 4, 10, 17),
        arrayListOf(8, 13, 16),
        arrayListOf(1, 6, 14, 19)
    )

    val tripsPassengers = arrayListOf<ArrayList<Int>>(
        arrayListOf(18, 3, 16, 12),
        arrayListOf(7, 9, 13, 14),
        arrayListOf(1, 0, 8, 19),
        arrayListOf(4, 5, 17, 6),
        arrayListOf(15),
        arrayListOf(11, 12, 2),
        arrayListOf(18, 3, 7),
        arrayListOf(0, 5, 10, 13, 17),
        arrayListOf(8, 14, 19),
        arrayListOf(2, 6, 11, 16)
    )

    val listTripsData: ArrayList<Trip>
        get(){
            val list = arrayListOf<Trip>()
            for (position in tripsImage.indices){
                val trip = Trip(
                    id = position,
                    name = tripsName[position],
                    description = tripsDescription[position],
                    image = tripsImage[position],
                    price = tripsPrice[position],
                    place = tripsPlace[position]
                )
                list.add(trip)
            }
            return list
        }

    fun getSpecificTrip(tripID: Int): Trip{
        val trip = Trip(
            id = tripID,
            name = tripsName[tripID],
            description = tripsDescription[tripID],
            image = tripsImage[tripID],
            price = tripsPrice[tripID],
            place = tripsPlace[tripID]
        )
        return trip
    }

}