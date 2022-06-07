import Vue from 'vue';
import _ from 'lodash';

export const state = () => ({
  technicians: []
})

export const mutations = {
  SET(state, technicians) {
    const data=[];
    for(const key in technicians.data){
      data.push({...technicians.data[key],id:key})
    }
    state.technicians = data;
  },
  ADD(state, technician) {
    state.technicians = state.technicians.concat(technician);
  },
  DELETE(state, technicianId) {
    state.technicians = state.technicians.filter(i => i.id !== technicianId);
  },
  EDIT(state, newTechnician) {
    let iIndex = state.technicians.findIndex(i => i.id === newTechnician.id);
    Vue.set(state.technicians, iIndex, newTechnician);
  }
}

export const actions = {
  async loadAllTechnicians({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants.json', params);
    commit('SET', response);

  },
  async create({commit}, technician) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants.json', technician);
    commit('ADD', {...technician,id:response.data.name});
  },
  async delete({commit}, technician) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants/${technician.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', technician.id);
    }
  },
  async edit({commit}, technician) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Technician Applicants/${technician.id}.json`, technician);
    let newTechnician = response.data;
    commit('EDIT', newTechnician);
  },
}

export const getters = {
  get: state => id => {
    return state.technicians.find(i => i.id === id) || {}
  }
}
