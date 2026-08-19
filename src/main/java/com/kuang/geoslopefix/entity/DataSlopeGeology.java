package com.kuang.geoslopefix.entity;

import lombok.Data;

@Data
public class DataSlopeGeology {
    private String id;
    private String slopeUnitId;
    private String slopeType;
    private String waterRiverValley;
    private String topElevation;
    private String bottomElevation;
    private String slopeLength;
    private String slopeShape;
}