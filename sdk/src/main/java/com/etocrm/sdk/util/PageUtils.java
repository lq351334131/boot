package com.etocrm.sdk.util;

import java.util.List;

/*
 * $
 * @author xing.liu
 * @分页组装
 */
public class PageUtils { 
    private List lists;// 存放需要显示的实体类数据
    private Integer pageIndex=1;// 当前页码数（默认给1），需要传参
    private Integer pageSize=10; // 每页显示的行数，需要传参
    private Integer count;// 总页数，是根据总行数和每页显示的行数计算出来的结果
    private Integer totalNum;// 总行数，总行数是查询出来的数据表总记录数
    private Integer limit;

    public List getLists() {
        return lists;
    }

    public void setLists(List lists) {
        this.lists = lists;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    /**
     * 设置总行数据并求出总页数
     *
     * @param totalNum 此参数是总行数
     */
    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
        //页数根据传入的总行数以及每页显示的行数，求出总页数
        this.count = totalNum % pageSize == 0 ? totalNum/ pageSize : totalNum/ pageSize + 1;
    }

    /**
     * 设置页码
     *
     * @param pageIndex 当前页数
     */
    public void setPageIndex(Integer pageIndex) {
        //如果传入的页码为空或者小于0  就默认给1
        if (null == pageIndex || pageIndex < 0) {
            this.pageIndex = 1;
            //如果当前页码数大于总页码数，就让当前页码数等于最大页码数
        } else if (pageIndex > this.count && this.count > 0) {
            this.pageIndex = this.count;
        } else {
            this.pageIndex = pageIndex;
        }
    }

    public Integer getLimit(){
        return  (pageIndex-1)*pageSize;
    }

    public  PageUtils (Integer pageSize,Integer pageIndex,Integer totalNum){
        this.pageSize=pageSize;
        this.pageIndex=pageIndex;
        this.totalNum=totalNum;

    }


}
