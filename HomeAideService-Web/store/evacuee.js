import Vue from 'vue';

export const state = () => ({
  evacuees: []
})

export const mutations = {
  SET(state, evacuees) {
    const data=[];
    for(const key in evacuees.data){
      data.push({...evacuees.data[key],id:key})
    }
    state.evacuees = data;
  },
  ADD(state, evacuee) {
    state.evacuees = state.evacuees.concat(evacuee);
  },
  DELETE(state, evacueeId) {
    state.evacuees = state.evacuees.filter(i => i.id !== evacueeId);
  },
  EDIT(state, newEvacuee) {
    let iIndex = state.evacuees.findIndex(i => i.id === newEvacuee.id);
    Vue.set(state.evacuees, iIndex, newEvacuee);
  }
}

export const actions = {
  async loadAllEvacuees({commit, dispatch}, params) {
    let response = await this.$axios.get('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects.json', params);
    commit('SET', response);

  },
  async create({commit}, evacuee) {
    let response = await this.$axios.post('https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects.json', evacuee);
    commit('ADD', {...evacuee,id:response.data.name});
  },
  async delete({commit}, evacuee) {
    let response = await this.$axios.delete(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects/${evacuee.id}.json`);
    if (response && _.includes([200, 204], response.status)) {
      commit('DELETE', evacuee.id);
    }
  },
  async edit({commit}, evacuee) {
    let response = await this.$axios.put(`https://homeaide-post-default-rtdb.asia-southeast1.firebasedatabase.app/Projects/${evacuee.id}.json`, evacuee);
    let newEvacuee = response.data;
    commit('EDIT', newEvacuee);
  },
}

export const getters = {
  get: state => id => {
    return state.evacuees.find(i => i.id === id) || {}
  }
}
