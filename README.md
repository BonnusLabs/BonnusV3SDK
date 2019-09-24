# SDK FOR BONNUS V3

The SDK is intended to consume the API of Bonnus V3 in a simple way to give Rewards to users and feed the Native WebView for visualizing the rewards flow.

## GRADLE IMPLEMENTATION

```
implementation 'mx.bonnusv3.bonnuslabs:bonnussdk:1.0.1'
```
## INITIALIZE THE SDK

The initialize method of the SDK starts the flow of the API. <br/><br/>
_Warning: this method must be called only once and not in every app session, to check if the method should be called, refer to __getUserId__ instructions_

```java
  Bonnus.getInstance().requestInitialize(
                  uniqueUserId,
                  dataDictionary,
                  accessToken,
                  context,
                  encryptData,
                  new Bonnus.OnBonnusSdkInitResponse() {
                      @Override
                      public void initializeResponse(Object value, String responseMessage) {
                          if(value != null){
                              //Successfully Initialized
                              Toast.makeText(context, value.toString(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  }
          );     
```

_requestInitialize Parameters:_

  __- uniqueUserId:__ a string that will indentify a unique user for all the rewards, provided by the Partner <br />
  __- dataDictionary:__ a dictionary to fill with the user information, used for data segmentation of the reward <br />
  - birthDate, zipCode, gender, geo, email, name, lastName, city, state <br />

  ```java
  Dictionary<String, String> dictionary = new Hashtable<>();
          dictionary.put("name","John");
          dictionary.put("lastName","Doe");
  ```
  __- accessToken:__ the Token to identify the Partner <br />
  __- context:__ View or LifeCycle Context <br />
  __- encryptData:__ Boolean value to specify if segmentetion data should be encrypted <br />
  __- callback Initialize Response:__ result of the API call <br />
  - value: is the **Id of a user** created by Bonnus, this is used to keep track of the user on next API calls using the SDK<br />


## GET EXISTING USER ID FROM LOCAL

When the API is initialized using the SDK an user is created in Bonnus V3 and saved locally in the SDK, this method will retrieve the userId that indicates that the SDK has been initialized, if its null or empty the SDK has not been initialized.

```java
  String value = Bonnus.getInstance().getUserId(context);

  if(value != null && !value.isEmpty()){
      Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
  } else {
      Toast.makeText(context, "No Sdk User", Toast.LENGTH_SHORT).show();
  }
```

## SEND A TRIGGERED MOMENT TO API FROM SDK

Moments are triggered and sent to the API using the SDK, if the user has won a reward the response value will be the URL Path to feed a Native Webview, oherwise this value will be empty but has be counted. The Web Application should be loaded in a WebView tha will manage the reward flow for the user.

```java
Bonnus.getInstance().sendMoment(
                momentTag,
                context,
                new Bonnus.OnBonnusSdkMomentResponse() {
                    @Override
                    public void momentResponse(Object value, String responseMessage) {
                        if(value != null && !value.toString().isEmpty()){
                            Intent intent = new Intent(context, WebAppActivity.class);
                            intent.putExtra(URL_TO_LOAD, value.toString());
                            startActivity(intent);
                        }
                    }
                }
        );
```
_sendMoment Parameters:_

__- momentTag:__ the tag configured for the moment to be rewarded<br />
__- context:__ View or LifeCycle Context<br />
__- momentResponse:__ the response every time a moment is triggered<br />
  - value has the Url of the Web Application, if it is empty, the reward was not earned yet, but counted as trigger<br />


## FETCH THE USER REWARDS

List of Rewards a user has earned could be fetched as a URL web Path to load in a Webview or a Java Object list for intern Handling.

```java
Bonnus.getInstance().requestRewards(asUrl, context, new Bonnus.OnBonnusSdkListResponse() {
            @Override
            public void listResponse(Object value, String responseMessage) {
                if(value instanceof String){
                    Intent intent = new Intent(context, WebAppActivity.class);
                    intent.putExtra(URL_TO_LOAD,value.toString());
                    startActivity(intent);
                } else if(value instanceof ArrayList){
                    Toast.makeText(context, "List of Rewards as List", Toast.LENGTH_SHORT).show();
                }
            }
        });
```
_requestRewards Parameters:_

__- asUrl:__ Boolean to specify to get user Rewards as a URL Web Path or a Java Object List <br />
__- context:__ View or LifeCycle Context<br />
__-rewardsResponse:__ callback containing the format of the rewards
