package com.ovov.lfzj.http.download;

/**
 * Created by kaite on 2018/9/26.
 */

public interface JsDownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
}
