# Tv Advertisement Shop


# Usage
 ## shop-backend:
- setup db connection in `application.properties`
- generate ssh key and save into `resorces/keys` directory (`jwt.pem` and `jwt.pub`)
- run `resorces/data.sql` to generate required roles
- create new user using api `api/auth/signup` or via frontend app
- grant `ROLE_ADMID` role to some user via db tool (e.g. DBeaver, intellij db plugin)

## shop-fronted:
- navigate to shop-frontend directory `cd shop-frontend`
- run `npm install`
- run `npm start`


## Plugins
* In shop-backend `resorces/plugin-examples` directory you can find ready to use jar plugins
* To create your own plugin use `TvChannelAdvertisementProvider` interface from `tv-channel-spi` module.
  (see `simple-tv-channel` and `xyz-tv-channel` implementation)
