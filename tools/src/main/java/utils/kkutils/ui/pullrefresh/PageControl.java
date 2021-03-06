package utils.kkutils.ui.pullrefresh;

import java.io.Serializable;
import java.util.List;

import utils.kkutils.common.MapListTool;

/**
 * Created by ishare on 2016/6/7.
 */
public class PageControl<T> implements Serializable {
    public static int noMoreDataPage=-1;//没有更多数据的时候 的当前页

    public MapListTool<T> mapListTool = new MapListTool<T>();



    public static int defaultPageSize=20;//至少要超过手机一屏幕的显示
    public int initPageSize = defaultPageSize;
    public int pageSize = initPageSize;//一页的数据大小
    public int firstPageNum = 1;//第一页的 索引
    public int currPageNum = firstPageNum;//当前是第几页

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    boolean hasMoreData=true;//是否还有下一页
    public static void setDefaultPageSize(int defaultPageSize) {
        PageControl.defaultPageSize = defaultPageSize;
    }
    public PageControl() {
        init();
    }

    public PageControl(int pageSize) {
        this.initPageSize = pageSize;
        this.pageSize = initPageSize;
        init();
    }

    public int init() {
        pageSize = initPageSize;
        currPageNum = firstPageNum;
        mapListTool.clear();
        return currPageNum;
    }

    /***
     * 设置当前是哪一页
     *
     * @param currPageNum
     */
    public void setCurrPageNum(int currPageNum) {
        this.currPageNum = currPageNum;
    }

    /***
     * 设置当前是哪一页
     *
     * @param currPageNum
     */
    public void setCurrPageNum(int currPageNum, List<T> datas) {
        this.currPageNum = currPageNum;
        if (datas != null) mapListTool.putList(currPageNum, datas);
        if(datas==null||datas.size()<getPageSize()){//没有下一页了
            hasMoreData=false;
        }else {
            hasMoreData=true;
        }
    }

    /***
     * 获取当前所有数据
     *
     * @return
     */
    public List<T> getAllDatas() {
        return mapListTool.getList(MapListTool.allGroup);
    }
    /***
     * 获取当前所有数据 反序，用于聊天记录
     *
     * @return
     */
    public List<T> getAllDatasRevers() {
        return mapListTool.getListRevers(MapListTool.allGroup);
    }
    /**
     * 获取下一页应该的索引
     *
     * @return
     */
    public int getNextPageNum() {
        if(!hasMoreData)return noMoreDataPage;
        return currPageNum + 1;
    }

    /***
     * 获取一页的数据数量
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 获取第一页 索引
     *
     * @return
     */
    public int getFirstPageNum() {
        return firstPageNum;
    }

    public int getCurrPage() {
        return currPageNum;
    }
}
