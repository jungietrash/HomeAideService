<template>

</template>

<script>
export default {
  asyncData() {
    $nuxt.$fire.auth.signOut();
    $nuxt.$router.push('/auth/signin')
  }
}
</script>

<style>

</style>
