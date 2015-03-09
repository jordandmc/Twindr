//
//  SettingsViewController.swift
//  Twindr
//
//  Created by Morgan Epp on 2015-02-12.
//  Copyright (c) 2015 Group1. All rights reserved.
//

import UIKit

class SettingsViewController: ViewController {
    
    @IBOutlet weak var genderField: UITextField!
    @IBOutlet weak var dobField: UITextField!
    @IBOutlet weak var interestsField: UITextField!
    var datePickerView: UIDatePicker = UIDatePicker()
    var genderPicker: UIPickerView = UIPickerView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set the input for the Date of Birth field
        self.datePickerView.datePickerMode = UIDatePickerMode.Date
        dobField.inputView = self.datePickerView
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveButton(sender: UIButton) {
        // save the changes
    }
    
    @IBAction func editDobField(sender: UITextField) {
        self.datePickerView.addTarget(self, action: Selector("handleDatePicker:"), forControlEvents : UIControlEvents.ValueChanged)
    }
    
    func handleDatePicker(sender: UIDatePicker) {
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateStyle = .ShortStyle
        dateFormatter.dateFormat = "yyyy/MM/dd"
        dobField.text = dateFormatter.stringFromDate(sender.date)
    }
    
    @IBAction func editGenderField(sender: UITextField) {
        // This may not be the way to go about for UIPickerView
        // You may have to override it's method to give it data
    }
    
    
}
