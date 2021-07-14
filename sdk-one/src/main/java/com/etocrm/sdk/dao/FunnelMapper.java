package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.funnel.*;

import java.util.List;
import java.util.Map;

public interface FunnelMapper {

    void insert(FunnelIdVO funnelIdVO);

    void batchInsert(List<FunnelSetpsIdVO> detailsList);

    void editFunnel(FunnelEditVO funnelEditVO);

    void deleteId(String id);

    void deleteDetailId(String id);

    FunnelSingleRepVO getId(String id);

    List<FunnelSetpsIdVO> getDetailId(String id);

    Integer getFunnelCount(FunnelVO funnelVO);

    List<FunnelRepVO> getFunnelPage(FunnelVO funnelVO);

    List<Map<String,Object>> getFunnPage(Map<String, Object> map);

}
