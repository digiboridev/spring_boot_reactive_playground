docker build -t rxpg .
docker run --name rxpg -d --env-file .env -p 8080:8080 rxpg