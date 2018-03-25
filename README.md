# manage-booking


## Requirements:
- Java 1.8
- Mysql 5.7+
- Maven
```bash
$ sudo apt-get update
$ sudo apt-get install mysql-server
$ sudo apt-get install maven
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer
```

## Installations:
```bash
$ git clone https://github.com/lon-wolf/manage-booking
$ cd manage-booking
$ mvn clean install
$ cd target
$ nohup java -cp manage-booking-1.0.0.jar:lib/* com.booking.config.ApplicationConfig &
```

## Points / Assumptions:
- Can view logs at : /tmp/booking/logs/service.log
- For single day update: start_date and end_date should be same in update API below.
- New updates will overwrite old entries, for example:
   - if 1st March, 2017 to 4th March, 2017 price is 1000
     and second update is make price 2000 from 3rd March, 2017 to 7th March, 2017.
     Then, final will be - price is 1000 from 1st to 2nd and 2000 from 3rd to 7th.

## APIs

### Update calender API

- Uri: `/api/update`
- Method : `POST`
- Headers :
    - `Content-Type` : `application/json`
- Request body:
    - availabilty: 
        - Integer
        - update availability or number of rooms
    - price:
        - Integer 
        - updated price
        - Either price or availability should be not null 
    - start_date:
        - Type: String
        - Format: “YYYY-MM-DD”
        - start interval of update, it’s mandatory
    - end_date:
        - Type: String
        - Format: “YYYY-MM-DD”
        - end interval of update, it’s mandatory
    - days:
        - List of Integer
        - Represent refine days
        - Empty will consider as ALL_DAYS.
        - Corresponding Integers:
          SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7), WEEKENDS(8), WEEKDAYS(
		      9), ALL_DAYS(10);

    - room_type:
        - Integer
        - For single: 1, double: 2
        - Mandatory
- Response :
    - Message: 
        - String



### Find Details API
  - Uri: /api/find
  - Method : GET
  - Params
    - start_date
    - end_date
  - Response : 
    - List of info of each day between start and end date



Postman API link: https://www.getpostman.com/collections/69b9c231a5dad44e3696 

### Sample:

Update API:
Request: 
```json
{
  "availabilty": 22,
  "price": 777,
  "start_date": "2017-32-07",
  "end_date": "20172-03-10",
  "days": [1],
  "room_type": 1
}
```
Response:
```json
{
  "message": "SUCCESS"
}
```



Find API:
  - Request: http://127.0.0.1:8080/api/find?start_date=2017-02-28&end_date=2017-03-12
  - Response:

```json
{  
   "daysList":[  
      {  
         "singleType":{  
            "rooms":5,
            "price":500
         },
         "doubleType":{  
            "rooms":5,
            "price":1000
         },
         "date":"Tue Feb 28 00:00:00 IST 2017"
      },
      {  
         "singleType":{  
            "rooms":33,
            "price":333
         },
         "doubleType":{  
            "rooms":5,
            "price":1000
         },
         "date":"Wed Mar 01 00:00:00 IST 2017"
      },
      {  
         "singleType":{  
            "rooms":33,
            "price":333
         },
         "doubleType":{  
            "rooms":5,
            "price":1000
         },
         "date":"Thu Mar 02 00:00:00 IST 2017"
      }
   ],
   "message":"SUCCESS"
}
```


