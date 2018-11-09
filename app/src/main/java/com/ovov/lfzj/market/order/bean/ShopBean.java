package com.ovov.lfzj.market.order.bean;

/**
 * Created by kaite on 2018/9/21.
 */

public class ShopBean {

    /**
     * orderid : 270412
     * orderno : SW201605200852401598033511
     * servicestation : 一家水果
     * orderstatus : 0
     * paymentmethod : --
     * payprice : 0
     * addtime : 2016-05-20 20:52:40
     * transfee : 0
     * totalprice : 0
     * goods_img : http://dfs.jianyezuqiu.cn:8090/group2/M01/0B/34/CqwC8Fch7UyAM-wtAAzXIni84dA612.jpg
     * goods_name : 建业足球POLO衫
     * goods_num : 1
     */
    private String id;
    private String name;
    private String price;
    private String img;
    private String url;
    private int orderid;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImg() {
        return img;
    }

    public String getUrl() {
        return url;
    }

    private String orderno;
    private String servicestation;
    private int orderstatus;
    private String paymentmethod;
    private int payprice;
    private String addtime;
    private int transfee;
    private int totalprice;
    private String goods_img;
    private String goods_name;
    private int goods_num;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getServicestation() {
        return servicestation;
    }

    public void setServicestation(String servicestation) {
        this.servicestation = servicestation;
    }

    public int getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(int orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public int getPayprice() {
        return payprice;
    }

    public void setPayprice(int payprice) {
        this.payprice = payprice;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getTransfee() {
        return transfee;
    }

    public void setTransfee(int transfee) {
        this.transfee = transfee;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }


}
