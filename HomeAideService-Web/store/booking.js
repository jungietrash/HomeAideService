import Vue from 'vue';
import _ from 'lodash';

export const state = () => ({
  bookings: []
})

export const mutations = {
  SET(state, bookings) {
    const data=[];
    for(const key in bookings.data){
      data.push({...bookings.data[key],id:key})
    }
    state.bookings = data;
  },
  ADD(state, booking) {
    state.bookings = state.bookings.concat(booking);
  },
  DELETE(state, bookingId) {
    state.bookings = state.bookings.filter(i => i.id !== bookingId);
  },
  EDIT(state, newBooking) {
    let iIndex = state.bookings.findIndex(i => i.id === newBooking.id);
    Vue.set(state.bookings, iIndex, newBooking);
  }
}

export const actions = {
  async loadAllBookings({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings.json', params);
    commit('SET', response);

  },
  async create({commit}, booking) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings.json', booking);
    commit('ADD', {...booking,id:response.data.name});
  },
  async delete({commit}, booking) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings/${booking.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', booking.id);
    }
  },
  async edit({commit}, booking) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings/${booking.id}.json`, booking);
    let newBooking = response.data;
    commit('EDIT', newBooking);
  },
}

export const getters = {
  get: state => id => {
    return state.bookings.find(i => i.id === id) || {}
  }
}
