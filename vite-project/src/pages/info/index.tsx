import { EllipsisVertical, User, Cake, VenusAndMars, MapPin, Mail } from "lucide-react";
import avatar from '../../assets/imgs/avatar-default.jpg';
import bg_info from '../../assets/imgs/bg-info.jpg';
import PersonalInfo from "./PersonalInfo";
import './index.css';

const Info = () => {
    const personalInfo = [
        { icon: <User size={16} />, label: "Tin Nguyen" },
        { icon: <Mail size={16} />, label: "tinnguyen210303@gmail.com" },
        { icon: <Cake size={16} />, label: "21/03/2003" },
        { icon: <VenusAndMars size={16} />, label: "Male" },
        { icon: <MapPin size={16} />, label: "Hue, Viet Nam" },
    ];

    return (
        <div className="d-flex flex-column">
            <div className="">
                <div className="user-profile-img relative">
                    <img src={bg_info} alt="bg" className="bg-info-img"/>
                    <div className="overlay-content">
                        <div className="d-flex justify-between align-center p-16 text-white">
                            <h1 className="fs-20 fw-medium">My Profile</h1>
                            <EllipsisVertical size={20} className="cursor-pointer"/>
                        </div>
                    </div>
                </div>
                <div className="d-flex flex-column align-center info-container">
                    <img src={avatar} alt="avatar" className="avatar-lg radius-50 mb-16"/>
                    <p className="mb-8 fs-16 fw-medium text-color">Tin Nguyen</p>
                    <p className="fs-14 fw-medium text-color-secondary">tinnguyen210303@gmail.com</p>
                </div>
            </div>
            <div className="d-flex flex-column p-24">
                <div className="mb-24">
                    <p className="fs-16 text-color-secondary">If several languages coalesce, the grammar of the resulting language is more simple.</p>
                </div>
                <PersonalInfo info={personalInfo} />
            </div>
        </div>
    )
}

export default Info;