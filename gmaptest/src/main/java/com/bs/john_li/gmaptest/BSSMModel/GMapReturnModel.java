package com.bs.john_li.gmaptest.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 2/8/2017.
 */

public class GMapReturnModel {

    /**
     * results : [{"address_components":[{"long_name":"277","short_name":"277","types":["street_number"]},{"long_name":"Bedford Avenue","short_name":"Bedford Ave","types":["route"]},{"long_name":"Williamsburg","short_name":"Williamsburg","types":["neighborhood","political"]},{"long_name":"Brooklyn","short_name":"Brooklyn","types":["sublocality","political"]},{"long_name":"Kings","short_name":"Kings","types":["administrative_area_level_2","political"]},{"long_name":"New York","short_name":"NY","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]},{"long_name":"11211","short_name":"11211","types":["postal_code"]}],"formatted_address":"277 Bedford Avenue, Brooklyn, NY 11211, USA","geometry":{"location":{"lat":40.714232,"lng":-73.9612889},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":40.7155809802915,"lng":-73.9599399197085},"southwest":{"lat":40.7128830197085,"lng":-73.96263788029151}}},"place_id":"ChIJd8BlQ2BZwokRAFUEcm_qrcA","types":["street_address"]}]
     * status : OK
     */

    private String status;
    private List<ResultsBean> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * address_components : [{"long_name":"277","short_name":"277","types":["street_number"]},{"long_name":"Bedford Avenue","short_name":"Bedford Ave","types":["route"]},{"long_name":"Williamsburg","short_name":"Williamsburg","types":["neighborhood","political"]},{"long_name":"Brooklyn","short_name":"Brooklyn","types":["sublocality","political"]},{"long_name":"Kings","short_name":"Kings","types":["administrative_area_level_2","political"]},{"long_name":"New York","short_name":"NY","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]},{"long_name":"11211","short_name":"11211","types":["postal_code"]}]
         * formatted_address : 277 Bedford Avenue, Brooklyn, NY 11211, USA
         * geometry : {"location":{"lat":40.714232,"lng":-73.9612889},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":40.7155809802915,"lng":-73.9599399197085},"southwest":{"lat":40.7128830197085,"lng":-73.96263788029151}}}
         * place_id : ChIJd8BlQ2BZwokRAFUEcm_qrcA
         * types : ["street_address"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String place_id;
        private List<AddressComponentsBean> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":40.714232,"lng":-73.9612889}
             * location_type : ROOFTOP
             * viewport : {"northeast":{"lat":40.7155809802915,"lng":-73.9599399197085},"southwest":{"lat":40.7128830197085,"lng":-73.96263788029151}}
             */

            private LocationBean location;
            private String location_type;
            private ViewportBean viewport;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class LocationBean {
                /**
                 * lat : 40.714232
                 * lng : -73.9612889
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":40.7155809802915,"lng":-73.9599399197085}
                 * southwest : {"lat":40.7128830197085,"lng":-73.96263788029151}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 40.7155809802915
                     * lng : -73.9599399197085
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 40.7128830197085
                     * lng : -73.96263788029151
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : 277
             * short_name : 277
             * types : ["street_number"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}
