<template>
  <v-container>
    <v-card>
      <v-card-title >
        Services
        <v-spacer></v-spacer>
      

        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn color="wizniche-blue" dark class="mb-2" v-on="on">New Service</v-btn>
          </template>


          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-form v-model="valid">
                  <v-row>
                    <v-text-field
                      v-model="editedItem.imageUrl"
                      label="imageUrl"
                      :rules="[required('imageUrl')]"
                    ></v-text-field>
                  </v-row>
                  
                  <v-row>
                    <v-text-field
                      v-model="editedItem.category"
                      label="category"
                      :rules="[required('category')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    
                    <v-btn-toggle   v-model="toggle_multiple"
                                  dense
                                  background-color="primary"
                                  dark
                                  multiple>
                      <v-btn v-model="editedItem.availableSun">
                        <v-icon>mdi-alpha-s-box</v-icon>
                      </v-btn>
                      <v-btn v-model="editedItem.availableMon">
                        <v-icon>mdi-alpha-m-box</v-icon>
                      </v-btn>
                      <v-btn v-model="editedItem.availableTue">
                        <v-icon>mdi-alpha-t-box</v-icon>
                      </v-btn>
                       <v-btn v-model="editedItem.availableWed">
                        <v-icon>mdi-alpha-w-box</v-icon>
                      </v-btn>
                       <v-btn v-model="editedItem.availableThu">
                        <v-icon>mdi-alpha-t-box</v-icon>
                      </v-btn>
                       <v-btn v-model="editedItem.availableFri">
                        <v-icon>mdi-alpha-f-box</v-icon>
                      </v-btn>
                       <v-btn v-model="editedItem.availableSat">
                        <v-icon>mdi-alpha-s-box-outline</v-icon>
                      </v-btn>
                    </v-btn-toggle>
                  </v-row>

                    <v-row>
                    <v-text-field
                      v-model="editedItem.startTime"
                      label="startTime"
                      :rules="[required('start Time')]"
                    ></v-text-field>
                   </v-row>
                   <v-row>
                    <v-text-field
                      v-model="editedItem.endTime"
                      label="endTime"
                      :rules="[required('endTime')]"
                    ></v-text-field>
                   </v-row>

                    <v-row>
                    <v-text-field
                      v-model="editedItem.percentageFee"
                      label="percentageFee"
                      :rules="[required('percentageFee')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.price"
                      label="price"
                      :rules="[required('price')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.projAddress"
                      label="projAddress"
                      :rules="[required('projAddress')]"
                    ></v-text-field>
                  </v-row>
                   <v-row>
                    <v-text-field
                      v-model="editedItem.projInstruction"
                      label="projInstruction"
                      :rules="[required('projInstruction')]"
                    ></v-text-field>
                  </v-row>
                   <v-row>
                    <v-text-field
                      v-model="editedItem.projName"
                      label="projName"
                      :rules="[required('projName')]"
                    ></v-text-field>
                  </v-row>
                   <v-row>
                    <v-text-field
                      v-model="editedItem.ratings"
                      label="rating"
                      :rules="[required('rating')]"
                    ></v-text-field>
                  </v-row>

                </v-form> 
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="save" :disabled="!valid">Save</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="services"
        :search="search"
        sort-by="name"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search Services"
              single-line
              hide-details
            ></v-text-field>
          </v-toolbar>
        </template>

        <template v-slot:[`item.imageUrl`]="{ item }">
         <img :src="item.imageUrl" width="100" height="100">
        </template>
        
        <template v-slot:[`item.actions`]="{ item }">

          <v-icon
            small
            class="mr-2"
            @click="editItem(item)"
          >
          mdi-pencil
          </v-icon>
          <v-icon
            small
            @click="deleteItem(item)"
          >
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import validations from "~/utils/validations";
import { mapState } from 'vuex';

export default {
  layout ({ layout }) {
    return layout;
  },
  data: () => ({
    valid: false,
    search: '',
    dialog: false,
    headers: [
      {
        text: 'Id',
        align: 'start',
        sortable: false,
        value: 'id',
      },
        {
        text: 'Image',
        align: 'start',
        width: 50,
        sortable: false,
        value: 'imageUrl',
      },
      {
        text: 'Category',
        align: 'start',
        sortable: false,
        value: 'category',
      },
      {
        text: 'Service Name',
        align: 'start',
        sortable: false,
        value: 'projName',
      },
      {
        text: 'Service Address',
        align: 'start',
        sortable: false,
        value: 'projAddress',
      },
      {
        text: 'Service Price',
        align: 'start',
        sortable: false,
        value: 'price',
      },
      {
        text: 'Service %',
        align: 'start',
        sortable: false,
        value: 'percentageFee',
      },
      {
        text: 'Start Service Time',
        align: 'start',
        sortable: false,
        value: 'startTime',
      },
      {
        text: 'End Service Time',
        align: 'start',
        sortable: false,
        value: 'endTime',
      },
      {
        text: 'Project Instruction',
        align: 'start',
        sortable: false,
        value: 'projInstruction',
      },
     {
        text: 'Project Rating',
        align: 'start',
        sortable: false,
        value: 'ratings',
      },



      { text: 'Actions', value: 'actions', sortable: false, width: '150' },
    ],
    editedIndex: -1,
    editedItem: {
      serviceName: '',
      firstName: '',
      lastName: ''
    },
    defaultItem: {
      serviceName: '',
      firstName: '',
      lastName: ''
    },
    ...validations
  }),
  async fetch({ store }) {
    try {
      await store.dispatch('service/loadAllServices');
    } catch (e) { }
  },
  computed: {
    ...mapState({
      services: state => state.service.services
    }),
    formTitle () {
      return this.editedIndex === -1 ? 'New Services' : 'Edit Services  '
    },
  },
  watch: {
    dialog (val) {
      val || this.close()
    },
  },
  created () {
  },
  methods: {
    editItem (item) {
      this.editedIndex = this.services.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      try {
        confirm('Are you sure you want to delete this item?') && await this.$store.dispatch('service/delete', item);
      } catch (e) { }
    },
    close () {
      this.dialog = false
      setTimeout(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      }, 300)
    },
    async save () {
      try {
        if (this.editedIndex > -1) {
          await this.$store.dispatch('service/edit', this.editedItem);
        } else {
          await this.$store.dispatch('service/create', this.editedItem);
        }
      } catch (e) { }
      this.close()
    },
  },
}
</script>

<style>
#app {
  background: url('https://wallpapercave.com/wp/wp4185683.jpg')
    no-repeat center center fixed !important;
  background-size: cover;
}
</style>