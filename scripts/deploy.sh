sudo docker ps -a -q --filter "name=app" | grep -q . && docker stop app && docker rm app | true

sudo docker rmi ohsesol/crab-coding-spring

sudo docker pull ohsesol/crab-coding-spring

docker run -d -p 80:8080 --name app ohsesol/crab-coding-spring

docker rmi -f $(docker images -f "dangling=true" -q) || true