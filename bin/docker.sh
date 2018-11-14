echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
cd Gatekeeper
docker build -t sem56402018/gatekeeper:$1 -t sem56402018/gatekeeper:$TRAVIS_COMMIT .
docker push sem56402018/gatekeeper:$TRAVIS_COMMIT
docker push sem56402018/gatekeeper:$1
