class GpsTransfer {
    var lat //gps로 반환받은 위도
            = 0.0
    var lon //gps로 반환받은 경도
            = 0.0
    private var xLat //x좌표로 변환된 위도
            = 0.0
    private var yLon //y좌표로 변환된 경도
            = 0.0

    constructor() {}
    constructor(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
    }

    fun getxLat(): Double {
        return xLat
    }

    fun getyLon(): Double {
        return yLon
    }

    fun setxLat(xLat: Double) {
        this.xLat = xLat
    }

    fun setyLon(yLon: Double) {
        this.yLon = yLon
    }

    //x,y좌표로 변환해주는것
    fun transfer(gpt: GpsTransfer, mode: Int) {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //
        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD
        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        if (mode == 0) {
//            rs.lat = lat_X; //gps 좌표 위도
//            rs.lng = lng_Y; //gps 좌표 경도
            var ra = Math.tan(Math.PI * 0.25 + gpt.lat * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = gpt.lon * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            val x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            val y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
            gpt.setxLat(x)
            gpt.setyLon(y)
            //            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
//            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        } else {
//            rs.x = lat_X; //기존의 x좌표
//            rs.y = lng_Y; //기존의 경도
            val xlat = gpt.getxLat()
            val ylon = gpt.getyLon()
            val xn = xlat - XO
            val yn = ro - ylon + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5
            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            //            rs.lat = alat * RADDEG; //gps 좌표 위도
//            rs.lng = alon * RADDEG; //gps 좌표 경도
            gpt.lat = alat * RADDEG
            gpt.lon = alon * RADDEG


            println(gpt.lat)
            println(gpt.lon)

        }
    }

    override fun toString(): String {
        return "GpsTransfer{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", xLat=" + xLat +
                ", yLon=" + yLon +
                '}'
    }
}