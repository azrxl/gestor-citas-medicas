import twitterIcon from '../assets/images/twitter.svg';
import facebookIcon from '../assets/images/facebook.svg';
import instagramIcon from '../assets/images/instagram.svg';

function Footer() {
    return (
        <footer className="footer-react">
            <div>
                Total Soft Inc.
                <div className="icons">
                    <img className="twitter" src={twitterIcon} alt="Twitter" />
                    <img className="facebook" src={facebookIcon} alt="Facebook" />
                    <img className="instagram" src={instagramIcon} alt="Instagram" />
                </div>
                <p>&copy; 2025 Tsf, Inc.</p>
            </div>
        </footer>
    );
}

export default Footer;