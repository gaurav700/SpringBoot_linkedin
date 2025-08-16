#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   ./build-images.sh                # builds with tag "local"
#   ./build-images.sh mytag          # builds with tag "mytag"
#   NO_CACHE=1 ./build-images.sh     # disable cache
#   ./build-images.sh local userservice  # build just one service

TAG="${1:-local}"
ONLY="${2:-}"
NO_CACHE="${NO_CACHE:-0}"

build() {
  local name="$1"
  local ctx="$2"
  local img="gaurav375/linkedin-app/${name}:${TAG}"

  echo ""
  echo "▶️  Building ${img} from ${ctx}"
  if [[ "$NO_CACHE" == "1" ]]; then
    DOCKER_BUILDKIT=1 docker build --no-cache -t "$img" "$ctx"
  else
    DOCKER_BUILDKIT=1 docker build -t "$img" "$ctx"
  fi
  echo "✅  Built ${img}"
}

declare -A MAP=(
  [apigateway]="./ApiGateway"
  [discoveryserver]="./DiscoveryServer"
  [connectionservice]="./ConnectionService"
  [notificationservice]="./NotificationService"
  [postservice]="./PostService"
  [userservice]="./UserService"
)

# build just one if provided
if [[ -n "$ONLY" ]]; then
  [[ -v MAP["$ONLY"] ]] || { echo "Unknown service: $ONLY"; exit 1; }
  build "$ONLY" "${MAP[$ONLY]}"
  exit 0
fi

# build all in a sensible order
build apigateway        "${MAP[apigateway]}"
build discoveryserver   "${MAP[discoveryserver]}"
build connectionservice "${MAP[connectionservice]}"
build notificationservice "${MAP[notificationservice]}"
build postservice       "${MAP[postservice]}"
build userservice       "${MAP[userservice]}"

echo ""
echo "🎉 All images built with tag: ${TAG}"
