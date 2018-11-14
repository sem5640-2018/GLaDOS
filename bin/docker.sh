echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t sem56402018/glados:$1 -t sem56402018/glados:$TRAVIS_COMMIT .
docker push sem56402018/glados:$TRAVIS_COMMIT
docker push sem56402018/glados:$1
