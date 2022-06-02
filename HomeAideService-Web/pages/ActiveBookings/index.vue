<template>
  <v-container>
    <v-card>
      <v-card-title>
       Active Bookings
        <v-spacer></v-spacer>
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
         <img :src="item.imageUrl" width="25" height="25">
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
