import Vue from 'vue';
import _ from 'lodash';

export const state = () => ({
  calamities: []
})

export const mutations = {
  SET(state, calamities) {
    const data=[];
    for(const key in calamities.data){
      data.push({...calamities.data[key],id:key})
    }
    state.calamities = data;
  },
  ADD(state, calamity) {
    state.calamities = state.calamities.concat(calamity);
  },
  DELETE(state, calamityId) {
    state.calamities = state.calamities.filter(i => i.id !== calamityId);
  },
  EDIT(state, newCalamity) {
    let iIndex = state.calamities.findIndex(i => i.id === newCalamity.id);
    Vue.set(state.calamities, iIndex, newCalamity);
  }
}

export const actions = {
  async loadAllCalamities({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants.json', params);
    commit('SET', response);

  },
  async create({commit}, calamity) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants.json', calamity);
    commit('ADD', {...calamity,id:response.data.name});
  },
  async delete({commit}, calamity) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants/${calamity.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', calamity.id);
    }
  },
  async edit({commit}, calamity) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants/${calamity.id}.json`, calamity);
    let newCalamity = response.data;
    commit('EDIT', newCalamity);
  },
}

export const getters = {
  get: state => id => {
    return state.calamities.find(i => i.id === id) || {}
  }
}
