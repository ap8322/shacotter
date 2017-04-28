docker exec -i $(docker ps | grep mysql | awk '{print $1}') mysql -e "drop database shacotter;"
docker exec -i $(docker ps | grep mysql | awk '{print $1}') mysql -e "create database shacotter;"
