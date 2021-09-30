# CRUD with Functional Programming in Scala
Sample project for FP with Scala Course

## Built With 🛠️
* [Scala](https://www.scala-lang.org/)

<!-- GETTING STARTED -->
## Deployment 🚀

1. Clone the repo
   ```sh
   git clone https://github.com/yonn28/dashDs4.git
   ```
2. Create virtual enviroment (please verify you have venv in your system)
   ```
   python3 -m venv dashDS4
   ```

### Docker testing local

```
docker build -t docker-ds4 .
docker run docker-ds4 -p 8080:8080
```
##### It's exposed at 8080 port in localhost

### Deploy GCP by hand

1. Setting default region

```
gcloud config set run/region northamerica-northeast1
 
 ```
2. Setting gcloud google container registry

```
gcloud builds submit --tag gcr.io/ds4all-deploy/dash-ds4-examaple  --project=ds4all-deploy
```


<!-- CONTACT -->
## Authors ✒️

* **Carlos Mendoza** 
* **Alejandro Rico**
* **Duván Agudelo**
* **David Quintero**

