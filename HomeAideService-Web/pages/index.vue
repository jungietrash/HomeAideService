<template>
  <div class="flex flex-column align-start align-self-start justify-start">
    <h1   >HomeAide Service Admin, Welcome back! {{ $nuxt.$fire.auth.currentUser.email }} to Admin Dashboard!</h1>
  </div>
</template>

<script>

export default {
  layout: 'default',
}
</script>

<style>
h1 {text-align: center;
color: black;
	font-size: 34px;
	-webkit-text-stroke: 1px black;
	-webkit-text-fill-color: white; 
}
p {text-align: center;}
div {text-align: center;}
#app {
  background: url('https://wallpapercave.com/wp/wp4185683.jpg')
    no-repeat center center fixed !important;
  background-size: cover;
}

</style>
