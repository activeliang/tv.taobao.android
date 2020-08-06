package com.google.zxing.client.result;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;

public final class GeoParsedResult extends ParsedResult {
    private final double altitude;
    private final double latitude;
    private final double longitude;
    private final String query;

    GeoParsedResult(double latitude2, double longitude2, double altitude2, String query2) {
        super(ParsedResultType.GEO);
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.altitude = altitude2;
        this.query = query2;
    }

    public String getGeoURI() {
        StringBuilder result = new StringBuilder();
        result.append("geo:");
        result.append(this.latitude);
        result.append(',');
        result.append(this.longitude);
        if (this.altitude > ClientTraceData.b.f47a) {
            result.append(',');
            result.append(this.altitude);
        }
        if (this.query != null) {
            result.append('?');
            result.append(this.query);
        }
        return result.toString();
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getQuery() {
        return this.query;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(20);
        result.append(this.latitude);
        result.append(", ");
        result.append(this.longitude);
        if (this.altitude > ClientTraceData.b.f47a) {
            result.append(", ");
            result.append(this.altitude);
            result.append('m');
        }
        if (this.query != null) {
            result.append(" (");
            result.append(this.query);
            result.append(')');
        }
        return result.toString();
    }
}
