<template>
  <v-app>
    <v-main >
      <!-- desktop -->
      <!-- <div class="d-none d-md-block"></div> -->
      <!-- mobile -->
      <div class="d-block">
        <v-container fluid class="jello-topbar">
          <v-row no-gutters align="center" justify="start">
            <v-col cols="1" class="flex-grow-0 flex-shrink-0">
              <v-icon @click="drawer = true">mdi-menu</v-icon>
            </v-col>
            <v-col
              cols="1"
              style="min-width: 100px; max-width:100%;"
              class="flex-grow-1 flex-shrink-0"
            >
              <v-row no-gutters align="center" justify="center">
                <nuxt-link to="/">
                  <v-row no-gutters align="center" justify="center" color="black"	font-size="50px">
                    <h3 class="logo">HomeAide Administrator</h3>
                  </v-row>
                </nuxt-link>
              </v-row>
            </v-col>
          </v-row>
        </v-container>
        <v-navigation-drawer v-model="drawer" fixed left class="d-block">
          <v-container fluid class="jello-topbar">
            <v-row no-gutters align="center" justify="space-between">
              <v-icon @click="drawer = false">mdi-close</v-icon>

              <v-row no-gutters align="center" justify="end">
                <p class="jello-user">Signed in as<br>
                  {{ $nuxt.$fire.auth.currentUser.email }}</p>
                &nbsp;
                <v-icon>mdi-account-circle-outline</v-icon>
              </v-row>

            </v-row>
          </v-container>
          <v-container class="d-block menu-items">
            <div class="d-flex flex-column">
              <div class="d-flex">
                <br />

              </div>
              <div class="d-flex">
                <nuxt-link to="/Technicians">
                  <v-icon>mdi-account-wrench</v-icon>&nbsp;&nbsp;<b>Technicians</b>
                </nuxt-link>
              </div>
              <div class="d-flex">
                <nuxt-link to="/ActiveBookings">
                  <v-icon>mdi-book-open-variant</v-icon>&nbsp;&nbsp;<b>Active Bookings</b>
                </nuxt-link>
              </div>
              <div class="d-flex">
                <nuxt-link to="/Services">
                  <v-icon>mdi-home-plus</v-icon>&nbsp;&nbsp;<b>Services</b>
                </nuxt-link>
              </div>
        
              <div class="d-flex">
                <nuxt-link to="/auth/signout">
                  <v-icon>mdi-logout-variant</v-icon>&nbsp;&nbsp;<b>Sign out</b>
                </nuxt-link>
              </div>
            </div>
          </v-container>
        </v-navigation-drawer>
      </div>
      <!-- content -->
      <nuxt />
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      drawer: false
    }
  },
}
</script>

<style lang="scss" scoped>
a {
  text-decoration: none;
}
.menu-items a {
  //color: $text-color;
  padding: 10px 0px 10px 3px;
  font-size: 24px;
}

</style>
