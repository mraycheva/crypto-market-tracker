# shellcheck disable=SC2046
docker rm -f -v $(docker container ls -a -q -f "label=maintainer="monicaraycheva@gmail.com"")
