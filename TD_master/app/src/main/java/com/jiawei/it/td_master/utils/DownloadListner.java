package com.jiawei.it.td_master.utils;

/**
 * @author JAIWEI
 * @company Thredim
 * @date on 2018/5/31.
 * @org www.thredim.com (宁波视睿迪光电有限公司)
 * @email thredim@thredim.com
 * @describe 添加描述
 */
public interface DownloadListner {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();
}
