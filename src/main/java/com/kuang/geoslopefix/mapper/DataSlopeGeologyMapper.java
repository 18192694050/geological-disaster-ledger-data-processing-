package com.kuang.geoslopefix.mapper;

import com.kuang.geoslopefix.entity.DataSlopeGeology;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DataSlopeGeologyMapper {

    int updateSlopeType(@Param("id") String id, @Param("slopeType") String slopeType);

    List<DataSlopeGeology> selectAllAbnormalSlopeType();
}