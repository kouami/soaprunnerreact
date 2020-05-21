import React, {Component} from 'react'

class URLAddressBar extends Component {
    render() {
        return (
            <div>
                <label htmlFor="exampleFormControlInput1">Email address</label>
                <input type="text" className="form-control" id="exampleFormControlInput1"
                       placeholder="name@example.com">
                </input>
            </div>
        );
    }
}

export default URLAddressBar;