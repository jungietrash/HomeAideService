import Vue from 'vue';
import _ from 'lodash';

export const state = () => ({
  evacuations: []
})

export const mutations = {
  SET(state, evacuations) {
    const data=[];
    for(const key in evacuations.data){
      data.push({...evacuations.data[key],id:key})
    }
    state.evacuations = data;
  },
  ADD(state, evacuation) {
    state.evacuations = state.evacuations.concat(evacuation);
  },
  DELETE(state, evacuationId) {
    state.evacuations = state.evacuations.filter(i => i.id !== evacuationId);
  },
  EDIT(state, newEvacuation) {
    let iIndex = state.evacuations.findIndex(i => i.id === newEvacuation.id);
    Vue.set(state.evacuations, iIndex, newEvacuation);
  }
}

export const actions = {
  async loadAllEvacuations({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings.json', params);
    commit('SET', response);

  },
  async create({commit}, evacuation) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings.json', evacuation);
    commit('ADD', {...evacuation,id:response.data.name});
  },
  async delete({commit}, evacuation) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings/${evacuation.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', evacuation.id);
    }
  },
  async edit({commit}, evacuation) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Bookings/${evacuation.id}.json`, evacuation);
    let newEvacuation = response.data;
    commit('EDIT', newEvacuation);
  },
}

export const getters = {
  get: state => id => {
    return state.evacuations.find(i => i.id === id) || {}
  }
}
