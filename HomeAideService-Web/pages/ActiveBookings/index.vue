<template>
  <v-container>
    <v-card>
      <v-card-title>
       Active Bookings
        <v-spacer></v-spacer>

       <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn color="wizniche-blue" dark class="mb-2" v-on="on">Add New Service Booking</v-btn>
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
                      v-model="editedItem.bookingTime"
                      label="Booking Time"
                      :rules="[required('Booking Time')]"
                    ></v-text-field>
                  </v-row>
            
                  <v-row>
                    <v-text-field
                      v-model="editedItem.bookingDate"
                      label="Booking Date"
                      :rules="[required('Booking Date')]"
                    ></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field
                      v-model="editedItem.bookingCreated"
                      label="Booking Created"
                      :rules="[required('Booking Created')]"
                    ></v-text-field>
                  </v-row>
                 <v-row>
                    <v-text-field
                      v-model="editedItem.custAddress"
                      label="Customer Address"
                      :rules="[required('Customer Address')]"
                    ></v-text-field>
                  </v-row>
                    <v-row>
                    <v-text-field
                      v-model="editedItem.custContactNum"
                      label="Customer Phone#"
                      :rules="[required('CCustomer Phone#')]"
                    ></v-text-field>
                  </v-row>
                   <v-row>
                    <v-text-field
                      v-model="editedItem.paymentMethod"
                      label="Payment Method"
                      :rules="[required('Payment Method')]"
                    ></v-text-field>
                  </v-row>
                    <v-row>
                    <v-text-field
                      v-model="editedItem.projName"
                      label="Service Name"
                      :rules="[required('Service Name')]"
                    ></v-text-field>
                  </v-row>
                    <v-row>
                    <v-text-field
                      v-model="editedItem.totalPrice"
                      label="Price"
                      :rules="[required('Price')]"
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
        :items="evacuations"  
        :search="search"
        sort-by="name"
        class="elevation-1"
      >


        <template v-slot:top>
          <v-toolbar flat>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search Bookings"
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
        value: 'imageUrl'
      },
      {
        text: 'Booking Time',
        align: 'start',
        sortable: false,
        value: 'bookingTime',
      },
      {
        text: 'Booking Date',
        align: 'start',
        sortable: false,
        value: 'bookingDate',
      },
      {
        text: 'Booking Created',
        align: 'start',
        sortable: false,
        value: 'bookingCreated',
      },
      {
        text: 'Customer Address',
        align: 'start',
        sortable: false,
        value: 'custAddress',
      },
      {
        text: 'Customer Phone#',
        align: 'start',
        sortable: false,
        value: 'custContactNum',
      },
      {
        text: 'Payment Method',
        align: 'start',
        sortable: false,
        value: 'paymentMethod',
      },
      {
        text: 'Service Name',
        align: 'start',
        sortable: false,
        value: 'projName',
      },
      {
        text: 'Price',
        align: 'start',
        sortable: false,
        value: 'totalPrice',
      },  




      
      { text: 'Actions', value: 'actions', sortable: false, width: '150' },
    ],
    editedIndex: -1,
    editedItem: {
      evacuationName: '',
      country: '',
      evacuationBarangay: ''
    },
    defaultItem: {
      evacuationName: '',
      country: '',
      evacuationBarangay: ''
    },
    ...validations
  }),
  async fetch({ store }) {
    try {
      await store.dispatch('evacuation/loadAllEvacuations');
    } catch (e) { }
  },
  computed: {
    ...mapState({
      evacuations: state => state.evacuation.evacuations
    }),
    formTitle () {
      return this.editedIndex === -1 ? 'New Evacuation' : 'Edit Evacuation'
    },
    // evacuations () {
    //   return {
    //     evacuations: this.$store.state.evacuation.evacuations
    //   }
    // }
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
      this.editedIndex = this.evacuations.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      try {
        confirm('Are you sure you want to delete this item?') && await this.$store.dispatch('evacuation/delete', item);
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
          await this.$store.dispatch('evacuation/edit', this.editedItem);
        } else {
          await this.$store.dispatch('evacuation/create', this.editedItem);
        }
      } catch (e) { }
      this.close()
    },
  },
}
</script>
