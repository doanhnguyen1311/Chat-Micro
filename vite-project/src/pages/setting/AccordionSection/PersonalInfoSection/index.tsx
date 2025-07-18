import { Pencil } from "lucide-react";

const PersonalInfoSection = () => {
  return (
    <div className="p-16 d-flex flex-column gap-20px fs-13 text-color-secondary">
      <div className="d-flex justify-between align-start">
        <div>
          <p className="fw-semibold text-black">Name</p>
          <p className="text-color mt-8">Tin Nguyen</p>
        </div>
        <button className="d-flex align-center py-8 px-12 btn-primary btn-green-hover">
          <Pencil size={14} />
        </button>
      </div>
      <div>
        <p className="fw-semibold text-black">Email</p>
        <p className="text-color mt-8">adc@123.com</p>
      </div>
      <div>
        <p className="fw-semibold text-black">BirthDay</p>
        <p className="text-color mt-8">21/03/2003</p>
      </div>
      <div>
        <p className="fw-semibold text-black">Gender</p>
        <p className="text-color mt-8">Male</p>
      </div>
      <div>
        <p className="fw-semibold text-black">Location</p>
        <p className="text-color mt-8">Hue, Viet Nam</p>
      </div>
    </div>
  );
};

export default PersonalInfoSection;
