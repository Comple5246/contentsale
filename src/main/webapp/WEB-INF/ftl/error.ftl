<!DOCTYPE html>
<html>
<#include "./include/head.ftl">
<body>
<#include "./include/support.ftl">
<#include "./include/header.ftl">
<div class="g-doc">
    <div class="n-result">
        <h3><#if errorMsg??>${errorMsg}<#else>系统错误，请稍后再试。</#if></h3>
    </div>
</div>
<#include "./include/footer.ftl">
<script type="text/javascript" src="/js/global.js"></script>
</body>
</html>