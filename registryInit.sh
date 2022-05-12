# 在自己的linux系统上安装docker
# 启动docker
systemctl start docker
# 拉取我放在dockerHub上的镜像
docker pull 836585692/zytregistry:1.0
# 根据镜像创建容器 映射对应的端口 即可使用
docker run -it -d -p 8848:8848 -p 2181:2181  --restart always 836585692/zytregistry:1.0

# 可以选择可以一键启动  但是建议还是分布 更加了解熟悉docker的运用
docker pull 836585692/zytregistry:1.0&&docker run -it -d -p 8848:8848 -p 2181:2181  --restart always 836585692/zytregistry:1.0