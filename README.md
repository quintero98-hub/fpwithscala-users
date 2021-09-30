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
# HTTP METHODS

## POST

### /users
* Summary: Create a user.
* Description: Validate that the user doesn't exist in the table and then insert the new user into it.
* Parameters: None
* Response:
   - In positive case it returns the user in JSON format.
   - "The user with legal id <legalId> already exists"
 
* Example:
      
```
gcloud builds submit --tag gcr.io/ds4all-deploy/dash-ds4-examaple  --project=ds4all-deploy
```
   
```
curl -X POST -d '{"legalId":"103","firstName":"Carlos","lastName":"Mendoza","email":"algo@gmail.com","phone":"3158453256"}' http://localhost:8000/users
```

## PUT

### /users/<legalId>
* Summary: Update USERS table
* Description: Validate that user exists in the database and overwrite its information with the new data in the corresponding row.
* Parameters: legalId
* Response: 
       - String with validation message: "The user with legalId <legalId> has been modified.". 
       - If the user wasn't found: "Couldn't find the user with legalID <legalId>"

## GET

### /users
* Summary: Retrieve all the users.
* Description: Retrieve all the users from the USERS table.
* Parameters: None
* Response: 
   - A list with all the users in JSON format. 
   - If there is no users it returns "There is no users created".

### /users/<legalId>
* Summary: Retrieve the specified user.
* Description: Retrieve the specified user from the USERS table.
* Parameters: legalId
* Response:
   - The user in JSON format
   - In negative case: "Couldn't find the user with legal id <legalId>"
   
## DELETE

### /users/<legalId>
* Summary: Delete the specified user.
* Description: Delete the specified user from the USERS table.
* Parameters: <legalId>
* Response: 
   - "User deleted"
   - Otherwise, it returns "Couldn't find the user with legal id <legalId>"


<!-- CONTACT -->
## Authors ✒️

* **Carlos Mendoza** 
* **Alejandro Rico**
* **Duván Agudelo**
* **David Quintero**

