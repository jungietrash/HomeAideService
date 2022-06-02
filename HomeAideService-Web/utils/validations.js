let required = (propertyType) => {
  return v => v && v.toString() && !!v.toString() || `${propertyType} is required`
};
let minLength = (propertyType, minLength) => {
  return v => v && v.length >= minLength || `${propertyType} must be at least ${minLength} characters`
};
let maxLength = (propertyType, maxLength) => {
  return v => v && v.length <= maxLength || `${propertyType} must be less than ${maxLength} characters`
};
let emailFormat = () => {
  let regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,24})+$/
  return v => v && regex.test(v) || "Email must be valid"
};
let hourFormat = () => {
  let regex = /^(((([0-1][0-9])|(2[0-3])):?[0-5][0-9])|(24:?00))/;
  return v => v && ( v.length > 3 && regex.test(v)) || "Invalid 24Hour format"
};
let minValue = (propertyType, minValue) => {
  return v => v && Number(v) >= minValue || `${propertyType} minimum value is ${minValue}`
};
let maxValue = (propertyType, maxValue) => {
  return v => v && Number(v) <= maxValue || `${propertyType} maximum value is ${maxValue}`
};
let minImgSize = () => {
  return value => !value || value.size < 10000000 || 'Image size should be less than 10MB'
}

export default {
  required,
  minLength,
  maxLength,
  emailFormat,
  hourFormat,
  minValue,
  minImgSize,
  maxValue,
}
