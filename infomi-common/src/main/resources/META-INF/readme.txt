# 是否启用验证
session.auth.enabled=true
# 不需要做登录态验证的列表(逗号分隔)
session.auth.exclude-list=/static/*,/css/*,/js/*


# 请求追踪配置
trace.enabled=true
trace.request=true
trace.response=true



# 打包部署到私有代码仓库
mvn install package -U

