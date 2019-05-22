<html>
    <#include "../common/hander.ftl">
<body>

<div id="wrapper" class="toggled">

    <#--边栏sidebar-->
    <#include "../common/nav.ftl">

    <#--一些主要内容content-->
     <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-striped table-condensed">
                        <thead>
                        <tr>
                            <th>商品ID</th>
                            <th>名称</th>
                            <th>图片</th>
                            <th>单价</th>
                            <th>库存</th>
                            <th>描述</th>
                            <th>类目</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list productInfoPage.content as productInfo>
                            <tr>
                            <td>${productInfo.productId}</td>
                            <td>${productInfo.productName}</td>
                            <td><img width="100" height="100" src="${productInfo.productIcon}"></td>
                            <td>${productInfo.productPrice}</td>
                            <td>${productInfo.productStock}</td>
                            <td>${productInfo.productDescription}</td>
                            <td>${productInfo.categoryType}</td>
                            <td>${productInfo.createTime}</td>
                            <td>${productInfo.updateTime}</td>
                            <td><a href="/sell/seller/product/index?productId=${productInfo.getProductId()}">修改</a></td>
                            <#if productInfo.getProductStatusEnum().getMessage()== "在架">
                                <td><a href="/sell/seller/product/off_sale?productId=${productInfo.getProductId()}">下架</a></td>
                                <#else>
                                <td><a href="/sell/seller/product/on_sale?productId=${productInfo.getProductId()}">上架</a></td>
                            </#if>

                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <#--分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1 >  <#--判断 当前页等于小于1 不可点击-->
                            <li class="disabled"><a href="#">上一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/product/List?page=${currentPage - 1}&size=${size}">上一页</a></li>
                        </#if>

                        <#--动态展示页数-->
                        <#list 1..productInfoPage.getTotalPages() as index>

                            <#if productInfoPage.getTotalPages() gt 10>


                                <#if index gt 5 && index lt productInfoPage.getTotalPages() - 5>
                                    <li class="disabled"> <a href="#">...</a> </li>

                                <#else>
                                    <#if currentPage == index> <#--判断 如果是当前页 不可点击-->
                                        <li class="disabled"> <a href="#">${index}</a> </li>
                                    <#else> <#--不是当前页 可点击-->
                                        <li> <a href="/sell/seller/product/List?page=${index}&size=${size}">${index}</a> </li>
                                    </#if>
                                </#if>

                            </#if>





                        </#list>

                        <#if  currentPage gte productInfoPage.getTotalPages()>
                            <li class="disabled"><a href="#">下一页</a></li>
                        <#else>
                            <li><a href="/sell/seller/product/List?page=${currentPage + 1}&size=${size}">下一页</a></li>
                        </#if>

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>




</body>
</html>
<#--
<#list orderDtoPage.content as orderDTO>
    ${orderDTO.orderId}<br>
-->
