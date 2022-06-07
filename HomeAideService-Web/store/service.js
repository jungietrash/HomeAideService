import Vue from 'vue';

export const state = () => ({
  services: []
})

export const mutations = {
  SET(state, services) {
    const data=[];
    for(const key in services.data){
      data.push({...services.data[key],id:key})
    }
    state.services = data;
  },
  ADD(state, service) {
    state.services = state.services.concat(service);
  },
  DELETE(state, serviceId) {
    state.services = state.services.filter(i => i.id !== serviceId);
  },
  EDIT(state, newService) {
    let iIndex = state.services.findIndex(i => i.id === newService.id);
    Vue.set(state.services, iIndex, newService);
  }
}

export const actions = {
  async loadAllServices({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects.json', params);
    commit('SET', response);

  },
  async create({commit}, service) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects.json', service);
    commit('ADD', {...service,id:response.data.name});
  },
  async delete({commit}, service) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects/${service.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', service.id);
    }
  },
  async edit({commit}, service) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects/${service.id}.json`, service);
    let newService = response.data;
    commit('EDIT', newService);
  },
}

export const getters = {
  get: state => id => {
    return state.services.find(i => i.id === id) || {}
  }
}
