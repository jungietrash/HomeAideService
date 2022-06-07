<template>
  <v-container>
    <v-card>
      <v-card-title>
        Employee
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn color="wizniche-blue" dark class="mb-2" v-on="on">Add New Technician</v-btn>
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
                      v-model="editedItem.firstName"
                      label="First Name"
                      :rules="[required('First Name')]"
                    ></v-text-field>
                  </v-row>

                  <v-row>
                    <v-text-field
                      v-model="editedItem.lastName"
                      label="Last Name"
                      :rules="[required('Last Name')]"
                    ></v-text-field>
                  </v-row>

             
                  <v-row>
                    <v-text-field
                      v-model="editedItem.phoneNumber"
                      label="Phone#"
                      :rules="[required('Phone#')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.selfieUrl"
                      label="Image Selfie"
                      :rules="[required('Image Selfie')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.validIDUrl"
                      label="Valid ID"
                      :rules="[required('Valid ID')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.id"
                      label="Technician UID"
                      :rules="[required('Technician UID')]"
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
        :items="technicians"
        :search="search"
        sort-by="name"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search Employee  "
              single-line
              hide-details
            ></v-text-field>
          </v-toolbar>
        </template>

        <template v-slot:[`item.selfieUrl`]="{ item }">
         <img :src="item.selfieUrl" width="100" height="100">
        </template>

          <template v-slot:[`item.validIdUrl`]="{ item }">
         <img :src="item.validIDUrl" width="200" height="100">
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
        <!--<template v-slot:no-data>-->
        <!--<v-btn color="primary" @click="initialize">Reset</v-btn>-->
        <!--</template>-->
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
        sortable: false,
        value: 'selfieUrl',
      },
   {
        text: 'Valid ID',
        align: 'start',
        sortable: false,
        value: 'validIdUrl',
      },
      {
        text: 'First Name',
        align: 'start',
        sortable: false,
        value: 'firstName',
      },
      {
        text: 'Last Name',
        align: 'start',
        sortable: false,
        value: 'lastName',
      },
      {
        text: 'Phone#',
        align: 'start',
        sortable: false,
        value: 'phoneNumber',
      },
      
      {
        text: 'Technician UID#',
        align: 'start',
        sortable: false,
        value: 'id',
      },


      { text: 'Actions', value: 'actions', sortable: false, width: '150' },
    ],
    editedIndex: -1,
    editedItem: {
    },
    defaultItem: {
    },
    ...validations
  }),
  async fetch({ store }) {
    try {
      await store.dispatch('technician/loadAllTechnicians');
    } catch (e) { }
  },
  computed: {
    ...mapState({
      technicians: state => state.technician.technicians
    }),
    formTitle () {
      return this.editedIndex === -1 ? 'New Technician' : 'Edit Technician'
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
      this.editedIndex = this.technicians.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      try {
        confirm('Are you sure you want to delete this item?') && await this.$store.dispatch('technician/delete', item);
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
          await this.$store.dispatch('technician/edit', this.editedItem);
        } else {
          await this.$store.dispatch('technician/create', this.editedItem);
        }
      } catch (e) { }
      this.close()
    },
  },
}
</script>
