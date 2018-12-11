<script id="${id}${random!}" name="${id}${random!}" type="text/plain" style="width:${width};height:${height};"></script>


<script type="text/javascript">
        $(function () {
            //实例化编辑器
            var ue${id}${random!} = UE.getEditor('${id}${random!}');
            function load_${id}${random!}() {
                return UE.getEditor('${id}${random!}').getContent();
            }
            <#if onBlurBack?? >
                <#if onBlurBack?length gt 1>
                    ue${id}${random!}.addListener("blur", function () {
                        ${onBlurBack}(UE.getEditor('${id}${random!}'));
                    });
                </#if>
            </#if>
            <#if onFocusBack?? >
                <#if onFocusBack?length gt 1>
                    ue${id}${random!}.addListener("focus", function () {
                        ${onFocusBack}(UE.getEditor('${id}${random!}'));
                    });
                </#if>
            </#if>

            <#if value?? >
                <#if value?length gt 1>
                    ue${id}${random!}.ready(function () {
                        ue${id}${random!}.setContent('${value}');  //赋值给UEditor
                    });
                </#if>
            </#if>
        });
</script>
