package com.company.nedbankhr20.viewmodel.customer

import android.app.Application
import android.os.Parcelable

import com.company.nedbankhr20.viewmodel.EntityViewModel
import com.sap.cloud.android.odata.espmcontainer.Customer
import com.sap.cloud.android.odata.espmcontainer.ESPMContainerMetadata.EntitySets

/*
 * Represents View model for Customer
 *
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and return the view model of that
 * type. This is because the ViewModelStore of ViewModelProvider cannot not be able to tell the difference between
 * EntityViewModel<type1> and EntityViewModel<type2>.
 */
class CustomerViewModel(application: Application): EntityViewModel<Customer>(application, EntitySets.customers, Customer.city) {
    /**
     * Constructor for a specific view model with navigation data.
     * @param [navigationPropertyName] - name of the navigation property
     * @param [entityData] - parent entity (starting point of the navigation)
     */
    constructor(application: Application, navigationPropertyName: String, entityData: Parcelable): this(application) {
        EntityViewModel<Customer>(application, EntitySets.customers, Customer.city, navigationPropertyName, entityData)
    }
}
