# shellcheck disable=SC2046
docker rmi -f $(docker images -f "label=maintainer=monicaraycheva@gmail.com" -qa)
